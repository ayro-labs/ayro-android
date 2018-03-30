## Configuring the project for development ##

First you need to define the API url to be used. To do this simply create the gradle.properties file and add the following content to the file, replacing <IP> by your IP address.
````
AYRO_API_URL=http://<IP>:3000
````

## Building for development environments ##

Just run the following command:
````
npm run build
````

## Building for production environments ##

Just run the following command:
````
npm run build-prod
````

## Releasing a new version ##

Just run the following command:
````
npm run release-lib -- major|minor|patch|version <version>
````

## Publishing the new version to Maven central ##
````
npm run publish-lib
````