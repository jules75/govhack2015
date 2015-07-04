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


; work in progress
#_(defn time-ago
  "Return human friendly string representation of how long ago unixtime was."
  [unixtime]
  (let [diff	(- (quot (System/currentTimeMillis) 1000) unixtime)
		minute	60
		hour 	(* 60 minute)
		day 	(* 24 hour)
		week 	(* 7 day)
		month 	(* 30 day)]
	(cond
	 (< diff minute) 	(str diff " second(s) ago")
	 (< diff hour) 		(str (quot diff minute) " minute(s) ago")
	 (< diff day) 		(str (quot diff hour) " hour(s) ago")
	 (< diff week) 		(str (quot diff day) " day(s) ago")
	 (< diff month) 	(str (quot diff week) " week(s) ago")
	 :default 			(str (quot diff month) " month(s) ago")
	 )))

