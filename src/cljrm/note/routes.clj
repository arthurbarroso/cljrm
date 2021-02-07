(ns cljrm.note.routes)


(defn routes
  [env]
  (let [db (:jdbc-url env)]
    [["/notes"
      {:get {
             :handler (fn [request] {:ok true})}}]]))