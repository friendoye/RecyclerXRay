
Contributing
============

Please make every effort to follow existing conventions and style in order to keep the code as
readable as possible.

If you have a feature request, please, [create an enhancement request](https://github.com/friendoye/RecyclerXRay/issues/new?assignees=&labels=enhancement&template=feature_request.md&title=).

If you’ve found a bug, please, [file an issue](https://github.com/friendoye/RecyclerXRay/issues/new?assignees=&labels=bug&template=bug_report.md&title=).

Contribute code changes through GitHub by forking the repository and sending a pull request. We
squash all pull requests on merge.

Code Contributions
------------------

Before submiting PR, please, make sure that all checks and tests are passing:

```
# Run various checks
./gradlew clean check
# Run Unit tests
./gradlew clean test
# Run Android tests
./gradlew connectedCheck
# Run Screenshot tests
# [Only if you're playing with screenshot testing] 
./gradlew executeScreenshotTests
```

Please note, that we use screenshot testing. Currently, screenshot tests are run on every PR submition and on push to `main` branch by Github Actions. If you faced some problems with passing screenshot tests or want to add one, [click here](docs/screenshot_tests.md).

Also, we use the [binary-compatibility-validator plugin](https://github.com/Kotlin/binary-compatibility-validator) for tracking the binary compatibility of the APIs we ship. If your changes imply changes to any public API, please run the following command to generate the updated API dumps and commit these changes:

```
./gradlew apiDump
```

Coding Style
------------

We use [Spotless](https://github.com/diffplug/spotless/tree/master) with [ktlint](https://github.com/pinterest/ktlint) for Kotlin code and some other files formatting. To make sure all files are correctly formated, please run the following command:

```
./gradlew spotlessApply
```