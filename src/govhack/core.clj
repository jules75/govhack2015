(ns govhack.core
  (:require [compojure.core :refer [defroutes routes GET]]
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


(defroutes app
  (GET "/" [] (find-places DB))
  (GET "/test" [] (main-template))
  (not-found "Page not found"))

