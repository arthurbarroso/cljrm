(ns cljrm.user.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(defn find-all-users [db]
  (->> (jdbc/execute! db ["SELECT * FROM users"]
                      {:builder-fn rs/as-unqualified-maps})
       (map (fn [user]
              (dissoc user :password)))))