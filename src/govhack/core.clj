(ns govhack.core
  (:require [clojure.string :refer [split-lines split] :as s]
			[clojure.data.csv :as csv]
			[clojure.java.io :as io]
			[compojure.core :refer [defroutes routes GET]]
			[compojure.route :refer [resources not-found]]
			;[compojure.handler :refer [site]]
			[yesql.core :refer [defqueries]]
			))


;(def data (with-open [in-file (io/reader "ballaratgraffitidefects.csv")]
;  (doall
;    (csv/read-csv in-file))))


(defqueries "sql/queries.sql")


(def DB {:subprotocol "mysql"
		 :subname "//localhost:3306/govhack"
		 :user "root"
		 :password "vmengrtbyj"})


(defroutes app
  (GET "/" [] (find-places DB))
  (not-found "Page not found"))

