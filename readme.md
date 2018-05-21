## Installing dependencies ##

````
npm install
````

## Configuring for development environment ##

Add the following property to your gradle.properties. This property should reference the URL of your Ayro server.
````
AYRO_API_URL=http://<IP>:<PORT>
````

## Building for development environment ##

````
npm run build
````

## Building for production environment ##

````
npm run build-prod
````

## Releasing a new version ##

````
npm run release-lib -- major|minor|patch|version
````

## Publishing the new version to Maven repository ##

````
npm run publish-lib
````