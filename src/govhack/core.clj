(ns govhack.core
  (:require [clojure.data.json :as json]
			[clojure.string :refer [join]]
			[compojure.core :refer [defroutes routes GET]]
			[compojure.route :refer [resources not-found]]
			[yesql.core :refer [defqueries]]
			[net.cgrand.enlive-html :as html]
			))


;(def data (with-open [in-file (io/reader "ballaratgraffitidefects.csv")]
;  (doall
;    (csv/read-csv in-file))))


(defqueries "sql/queries.sql")


(def DB {:subprotocol "mysql"
		 :subname "//localhost:3306/govhack"
		 :user "root"
		 :password "vmengrtbyj"})


(defn json-places
  []
  (->> (find-places DB) (map json/write-str) (join ",") (apply str)))


(html/deftemplate map-template "html/_layout.html"
  []
  [:#placeList]
  (html/content
   (str "var places=[" (json-places) "]")))


(html/deftemplate place-template "html/_layout.html"
  [id]
  [:#content]
  (html/content (find-place-by-id DB id)))


(defroutes app
  (GET "/" [] (map-template))
  (GET "/map" [] (map-template))
  (GET "/place/:id{[0-9]+}" [id] (place-template id))
  (resources "/")
  (not-found "Page not found")
  )

