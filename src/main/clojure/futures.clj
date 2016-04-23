(def f (future
         (Thread/sleep 3000)
         (.getName (Thread/currentThread))
         (+ 10 1000)
         ))

(println (realized? f))

(Thread/sleep 4000)

(println (realized? f))

(println (deref f))

(promise)

(deliver promise 10)

@promise

(deref promise)

(shutdown-agents)