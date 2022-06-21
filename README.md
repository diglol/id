# Diglol Id

Diglol Id is a global Id generator for Kotlin Multiplatform. It modifies the timestamp to 5 bytes based on [Xid].

> **Warning**: Currently not compatible with [Xid].

### Compare common global Id generators

|        Name | Binary Size | String Size    | Time Limit  | Features                                                       |
|------------:|-------------|----------------|-------------|----------------------------------------------------------------|
| [UUID]      | 16 bytes    | 36 chars       | /           | configuration free, not sortable                               |
| [shortuuid] | 16 bytes    | 22 chars       | /           | configuration free, not sortable                               |
| [Snowflake] | 8 bytes     | up to 20 chars | 69 years    | needs machine/DC configuration, needs central server, sortable |
| [MongoID]   | 12 bytes    | 24 chars       | 136 years   | configuration free, sortable                                   |
| [Xid]       | 12 bytes    | 20 chars       | 136 years   | configuration free, sortable                                   |
| [Diglol Id] | 13 bytes    | 21 chars       | 34865 years | configuration free, sortable                                   |

### Add Dependency

```gradle
implementation("com.diglol.id:id:0.1.0")
```

### Code Samples

```kotlin
val id = Id.generate()
println(id)
// 输出: 016ohoarc3q8dp1884msi
```

Get `id` embedded info:

```kotlin
id.machine
id.pid
id.time
id.counter
```

### License

    Copyright 2022 Diglol

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[UUID]: https://en.wikipedia.org/wiki/Universally_unique_identifier
[shortuuid]: https://github.com/stochastic-technologies/shortuuid
[Snowflake]: https://blog.twitter.com/2010/announcing-snowflake
[MongoID]: https://docs.mongodb.org/manual/reference/object-id/
[Xid]: https://github.com/rs/xid
