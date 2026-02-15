
# Hot to reproduce the issue

### Requirements
- JDK 21
- maven

### Run

```shell
mvn spring-boot:run
```

### See the output

It contains following warnings:                  

```
2026-02-15T15:24:58.986+01:00  WARN 4764 --- [           main] i.m.o.c.ObservationThreadLocalAccessor   : Observation <{name=test(null), error=null, context=name='test', contextualName='null', error='null', lowCardinalityKeyValues=[], highCardinalityKeyValues=[], map=[class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=0.012551542, duration(nanos)=1.2551542E7, startTimeNanos=851194955887166}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@6b867ee7'], parentObservation=null}> to which we're restoring is not the same as the one set as this scope's parent observation <null>. Most likely a manually created Observation has a scope opened that was never closed. This may lead to thread polluting and memory leaks.
2026-02-15T15:24:59.033+01:00  WARN 4764 --- [           main] i.m.o.c.ObservationThreadLocalAccessor   : There is no current scope in thread local. This situation should not happen
2026-02-15T15:24:59.033+01:00  WARN 4764 --- [           main] i.m.o.c.ObservationThreadLocalAccessor   : Observation <{name=test(null), error=null, context=name='test', contextualName='null', error='null', lowCardinalityKeyValues=[], highCardinalityKeyValues=[], map=[class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=0.059896417, duration(nanos)=5.9896417E7, startTimeNanos=851194955887166}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@6b867ee7'], parentObservation=null}> to which we're restoring is not the same as the one set as this scope's parent observation <null>. Most likely a manually created Observation has a scope opened that was never closed. This may lead to thread polluting and memory leaks.
```

# Workarounds

Each of these "solve" the issue (ie the warnings disappear):

1. Remove the `Hooks.enableAutomaticContextPropagation();` line
2. Remove the `io.micrometer:micrometer-tracing` library from dependencies
