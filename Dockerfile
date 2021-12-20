FROM node:17-alpine3.14 AS frontendBuild
WORKDIR /build
COPY package.json yarn.lock /build/
RUN yarn install --frozen-lockfile

COPY src ./src
COPY angular.json package.json tsconfig.json ./
RUN yarn build

FROM openjdk:11 AS backendBuild
WORKDIR /build
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw -DskipTests=true compile
COPY --from=frontendBuild /build/dist/mancala-ui ./src/main/resources/static
RUN ./mvnw -DskipTests=true package

FROM gcr.io/distroless/java11-debian11
WORKDIR /home/nonroot
COPY --from=backendBuild /build/target/mancala.jar .
CMD ["mancala.jar"]
