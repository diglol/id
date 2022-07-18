package diglol.id

import diglol.crypto.random.nextBytes
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@Measurement(iterations = 5, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
class IdBenchmark {
  lateinit var idString: String

  @Setup
  fun setup() {
    Id.machine = nextBytes(3)
    idString = Id.generate().toString()
  }

  @Benchmark
  fun generate() = Id.generate()

  @Benchmark
  fun generateToString() = Id.generate().toString()

  @Benchmark
  fun fromString() = Id.fromString(idString)
}
