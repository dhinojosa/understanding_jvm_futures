(def f (future
         (Thread/sleep 3000)
         (.getName (Thread/currentThread))
         (+ 10 1000)
         ))

(prn (realized? f))

(Thread/sleep 4000)

(prn (realized? f))

(prn (deref f))

(shutdown-agents)