# gradle-plugin-git-tag

### The plugin contains a task - "gitTask" that performs the following functions:

Depending on which branch the task was launched in:
* specifies the latest published version;
* determines the version of the current build based on the following logic: dev/qa - increments the minor version; stage - increments the minor version and adds a postfix -rc; master - increments the major version; from any other branch adds - postfix -SNAPSHOT;
* assigns the appropriate git tag;
* will publish it to origin.
* If the current state of the project has already been assigned a git tag, a new one should not be assigned
* If there are uncommitted changes in the working directory, log the build number with the postfix .uncommitted, do not create a git tag
