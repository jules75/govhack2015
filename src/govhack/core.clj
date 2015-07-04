(ns govhack.core
  (:require [clojure.data.json :as json]
			[clojure.string :refer [join]]
			[compojure.core :refer [defroutes routes GET POST]]
			[compojure.route :refer [resources not-found]]
			[yesql.core :refer [defqueries]]
			[net.cgrand.enlive-html :as html]
			))


(defqueries "sql/queries.sql")


(def DB {:subprotocol "mysql"
		 :subname "//localhost:3306/govhack"
		 :user "root"
		 :password "vmengrtbyj"})


(defn jsonify
  [coll]
  (->> coll (map json/write-str) (join ",") (apply str)))


(html/deftemplate map-template "html/_layout.html"
  []
  [:#placeList]
  (html/content
   (str "var places=[" (jsonify (find-places DB)) "]")))


(html/defsnippet
  place-snippet
  "html/place.html"
  [:#place]
  [place]
  [:h2] (html/content (:title place))
  )


(html/deftemplate place-template "html/_layout.html"
  [id]
  [:#content] (html/content (place-snippet (first (find-place-by-id DB id))))
  [:#placeList] (html/content (str "var places=[" (jsonify (find-place-by-id DB id)) "]"))
  )


(defroutes app
  (GET "/" [] (map-template))
  (GET "/map" [] (map-template))
  (GET "/place/:id{[0-9]+}" [id] (place-template id))
  ;(POST "/add/memory"
  (resources "/")
  (not-found "Page not found")
  )

