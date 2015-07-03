(ns govhack.core
  (:require [clojure.string :refer [split-lines split] :as s]
			[clojure.data.csv :as csv]
			[clojure.java.io :as io]
			[compojure.core :refer [defroutes routes GET]]
			[compojure.route :refer [resources not-found]]
			;[compojure.handler :refer [site]]
			))


;(def data (with-open [in-file (io/reader "ballaratgraffitidefects.csv")]
;  (doall
;    (csv/read-csv in-file))))


(defroutes app
  (GET "/" [] "<h1>Hello World</h1>")
  (not-found "<h1>Page not found</h1>"))

