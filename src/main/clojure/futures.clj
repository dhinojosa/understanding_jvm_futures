
;; Demo 12: Clojure Futures

(def f (future
         (Thread/sleep 3000)
         (.getName (Thread/currentThread))
         (+ 10 1000)
         ))

(println (realized? f))

(Thread/sleep 4000)

(println (realized? f))

(println (deref f))


;; Demo 13: Clojure Promise

(def p (promise))

(deliver p 10)

@p

;(deref p)

;(shutdown-agents)