## Formatting

Formatting is applied and/or validated automatically using a Git hook. The frontend build will set this up automatically on executing `yarn install`.

Alternatively, you can manually run `yarn format` to format both backend and frontend projects, or `./mvnw spotless:apply` to format the backend project.

## Testing

```shell
./mvnw test
yarn test
```

## Building

```shell
docker build -t mancala .
```

If you insist on building outside of docker, you can do the following:

```shell
yarn build
cp dist/mancala-ui/* src/main/resources/static
./mvnw package
```

The bundled application can then be found at `target/mancala.jar`.

## Running the app

After having built the docker image, run it as follows:

```shell
docker run -p 8080:8080 mancala
```

When not using the docker image, run the bundled application from the previous build step as follows:

```shell
java -jar target/mancala.jar
```

The application will be available at http://localhost:8080

Enjoy!
