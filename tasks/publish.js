/* eslint-disable import/no-extraneous-dependencies */

'use strict';

const packageJson = require('../package');
const {commands, publish} = require('release-n-publish');
const path = require('path');
const GitHubApi = require('@octokit/rest');

const WORKING_DIR = path.resolve();
const GITHUB_REPOSITORY_NAME = 'ayro-android';
const GITHUB_REPOSITORY_OWNER = 'ayrolabs';
const TEMP_DIR = '/tmp';
const TEMP_GITHUB_REPOSITORY_DIR = path.join(TEMP_DIR, GITHUB_REPOSITORY_NAME);

const gitHubApi = new GitHubApi();
gitHubApi.authenticate({
  type: 'token',
  token: process.env.GITHUB_ACCESS_TOKEN,
});

async function buildLibrary() {
  commands.log('Building library...');
  await commands.exec('npm run build-prod', WORKING_DIR);
}

async function prepareGithubRepository() {
  commands.log('Preparing Github repository...');
  await commands.exec(`rm -rf ${GITHUB_REPOSITORY_NAME}`, TEMP_DIR);
  await commands.exec(`git clone git@github.com:${GITHUB_REPOSITORY_OWNER}/${GITHUB_REPOSITORY_NAME}.git ${GITHUB_REPOSITORY_NAME}`, TEMP_DIR);
}

async function copyArtifactsToGithubRepository() {
  commands.log('Generating and copying artifacts to Github repository...');
  await commands.exec('./gradlew uploadArchives', WORKING_DIR);
}

async function pushArtifactsToGithubRepository() {
  commands.log('Committing and pushing artifacts to Github repository...');
  await commands.exec('git add --all', TEMP_GITHUB_REPOSITORY_DIR);
  await commands.exec(`git commit -am 'Release ${packageJson.version}'`, TEMP_GITHUB_REPOSITORY_DIR);
  await commands.exec('git push origin master', TEMP_GITHUB_REPOSITORY_DIR);
}

async function publishLibrary() {
  await prepareGithubRepository();
  await copyArtifactsToGithubRepository();
  await pushArtifactsToGithubRepository();
}

// Run this if call directly from command line
if (require.main === module) {
  publish.setWorkingDir(WORKING_DIR);
  publish.setBuildTask(buildLibrary);
  publish.setPublishTask(publishLibrary);
  publish.run();
}
