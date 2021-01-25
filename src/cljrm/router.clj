(ns cljrm.router
  (:require [reitit.ring :as ring]
            [reitit.dev.pretty :as pretty]
            [cljrm.jobs.routes :as jobs]))

(def router-config
  {:exception pretty/exception})

(defn routes [env]
  (ring/ring-handler
    (ring/router
      [["/v1"
        (jobs/routes env)]]
      router-config)))