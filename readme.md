## Running the project ##

First you need to define the API url to be used:

- Create the file gradle.properties.
- Add the following content to the file, replacing <IP> by your IP address.
````
systemProp.CHATZ_API_URL=http://<IP>:3000
````

## Building for development environment ##

Just run the following command:
````
./gradlew clean assembleDebug
````

## Building for production environment ##

Just run the following command:
````
./gradlew clean assembleRelease
````

## Releasing a new version ##

Just run the following command:
````
npm run release-lib -- <major|minor|patch>
````

## Publishing a new version ##
````
npm run publish-lib
````