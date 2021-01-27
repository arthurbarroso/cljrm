(ns cljrm.user.handler
  (:require [cljrm.user.db :as db]
            [ring.util.response :as rr]))

(defn find-users [db]
  (fn [request]
    (rr/response (db/find-all-users db))))