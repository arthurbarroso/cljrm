(ns cljrm.jobs.routes)

(defn routes
  [env]
  ["/jobs"
   {:get {:handler (fn [req]
                     {:status 200
                      :body "coolio"})}}])