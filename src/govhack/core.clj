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


(html/deftemplate main-template "html/_layout.html"
  []
  [:head :title] (html/content "Enlive starter kit"))


(html/deftemplate map-template "html/_layout.html"
  []
  [:#placeList]
  (html/content
   (let [s (->> (find-places DB) (map json/write-str) (join ",") (apply str))]
	 (str "var places=[" s "]"))
   ))


(defroutes app
  (GET "/" [] (map-template))
  (GET "/map" [] (map-template))
  (resources "/")
  (not-found "Page not found")
  )

