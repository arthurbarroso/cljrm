(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as state]
            [cljrm.server]))

(ig-repl/set-prep!
  (fn [] (-> "resources/config.edn" slurp ig/read-string)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :cljrm/app))
(def db (-> state/system :db/postgres))

(comment
  (go)
  (app {:request-method :get
        :uri "/v1/jobs"})
  (halt)
  (reset))