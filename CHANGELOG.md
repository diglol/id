# Change Log

## [Unreleased]

## [0.3.0] - 2023-02-25

### Changed

* Add getter for `Id.bytes` property. (#36)
* Avoid type casting (#29)
* Don't decode embedded info when creating `Id` (#28)
* Remove deprecated methods on `Id` (#27)

## [0.2.0] - 2022-10-10

### Changed

* Provide friendly api for JVM and Obj-C (#26)
* Remove superfluous bitmasking in encode (#24)
* Just need to check the last byte. (#22)
* Add `encodeToString` for `Id` (#18)

## [0.1.1] - 2022-07-17

### Fixed

* When the `machine` is not initialized, the `machine` is regenerated every time.

## [0.1.0] - 2022-07-16

Initial release.
