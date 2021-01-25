(ns cljrm.router
  (:require [reitit.ring :as ring]
            [reitit.dev.pretty :as pretty]
            [cljrm.jobs.routes :as jobs]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]))

(def swagger-docs
  ["/swagger.json"
   {:get {:no-doc true
          :swagger {:basePath "/"
                    :info {:title "CLJRM API"
                           :description "REST api for CLJRM"
                           :version "1.0.0"}}
          :handler (swagger/create-swagger-handler)}}])

(def router-config
  {:exception pretty/exception
   :data      {:muuntaja   m/instance
               :middleware [swagger/swagger-feature
                            muuntaja/format-middleware]}})

(defn routes [env]
  (ring/ring-handler
    (ring/router
      [swagger-docs
       ["/v1"
        (jobs/routes env)]]
      router-config)
    (ring/routes (swagger-ui/create-swagger-ui-handler
                   {:path "/"}))))