(ns govhack.data
  (:require [clojure.data.csv :as csv]
			[clojure.java.io :as io]
			[clojure.string :refer [join]]
			))


;
; Code in this file used to get data from CSV into database.
; Not used in final product.
;


#_(def data (with-open [in-file (io/reader "data/ballaratpropertybuiltyear.csv")]
			  (doall
			   (csv/read-csv in-file))))


; make SQL string
#_(apply str
	   (let [[header rows] ((juxt first rest) data)]
		 (for [[_ _ addr lng lat] (take 100 (shuffle rows))]
		   (str
			"INSERT INTO places (title, lat, lng) VALUES ('" addr "', " lat ", " lng ");")
		   )))
