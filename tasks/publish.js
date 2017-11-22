const utils = require('./utils');
const path = require('path');
const childProcess = require('child_process');
const propertiesParser = require('properties-parser');
const GitHubApi = require('github');
const Promise = require('bluebird');

const REPOSITORY_NAME = 'chatz-android';
const REPOSITORY_OWNER = 'chatz-io';
const WORKING_DIR = path.resolve(__dirname, '../');
const GRADLE_PROPERTIES = path.resolve(__dirname, '../gradle.properties');

const execAsync = Promise.promisify(childProcess.exec);

Promise.promisifyAll(propertiesParser);

const gitHubApi = new GitHubApi();
gitHubApi.authenticate({
  type: 'token',
  token: '49aa7a1e572c681a5f7773983359a57e71a15b30',
});

function exec(command, options) {
  return execAsync(command, options || {cwd: WORKING_DIR});
}

function checkoutTag(version) {
  return Promise.coroutine(function* () {
    console.log(`Checking out the tag ${version}...`);
    yield exec(`git checkout ${version}`);
  })();
}

function buildLibrary() {
  return Promise.coroutine(function* () {
    console.log('Building library...');
    yield exec('npm run build-prod');
  })();
}

function editGradleProperties(versionName, versionCode) {
  return Promise.coroutine(function* () {
    const properties = yield propertiesParser.createEditor(GRADLE_PROPERTIES);
    properties.set('VERSION_NAME', versionName);
    properties.set('VERSION_CODE', versionCode);
    properties.set('POM_GROUP_ID', 'io.chatz');
    properties.set('POM_ARTIFACT_ID', 'chatz');
    properties.set('POM_PACKAGING', 'aar');
    properties.set('POM_NAME', 'Chatz');
    properties.set('POM_DESCRIPTION', 'Chatz Android SDK');
    properties.set('POM_URL', 'https://chatz.io');
    properties.set('POM_DEVELOPER_ID', 'chatz');
    properties.set('POM_DEVELOPER_NAME', 'Chatz');
    properties.set('RELEASE_REPOSITORY_URL', 'https://oss.sonatype.org/service/local/staging/deploy/maven2');
    properties.set('SNAPSHOT_REPOSITORY_URL', 'https://oss.sonatype.org/content/repositories/snapshots');
    yield propertiesParser.save();
  })();
}

function publishToMavenCentral() {
  return Promise.coroutine(function* () {
    console.log('Publishing to Maven central...');
    yield exec('./gradlew uploadArchives');
  })();
}

// Run this if call directly from command line
if (require.main === module) {
  Promise.coroutine(function* () {
    try {
      const version = yield utils.getProjectVersion();
      const versionCode = yield utils.getProjectVersionCode();
      console.log(`Publishing version ${version} to Maven central...`);
      yield checkoutTag(version);
      yield buildLibrary();
      yield editGradleProperties(version, versionCode);
      yield publishToMavenCentral();
      yield checkoutTag('master');
      console.log(`Version ${version} published with success!`);
    } catch (err) {
      console.error(err);
      process.exit(1);
    }
  })();
}
