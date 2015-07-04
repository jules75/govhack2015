(ns govhack.core
  (:require [clojure.data.json :as json]
			[clojure.string :refer [join]]
			[compojure.core :refer [defroutes GET POST]]
			[compojure.route :refer [resources not-found]]
			[compojure.handler :refer [site]]
			[ring.util.response :refer [redirect]]
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
  [:#place-id] (html/set-attr :value (:id place)))


(html/defsnippet
  memory-snippet
  "html/memory.html"
  [:.memory]
  [memory]
  [:.details] (html/content (:details memory))
  [:.timestamp] (html/content (str (:created memory))))


(html/defsnippet
  photo-snippet
  "html/photo.html"
  [:.photo]
  [photo]
  [:img] (html/set-attr :src (:url photo))
  [:a] (html/set-attr :href (:url photo))
  [:.timestamp] (html/content (str (:created photo))))


(html/defsnippet
  poll-snippet
  "html/poll.html"
  [:.poll]
  [poll]
  [:.title] (html/content (:title poll))
  [(html/attr= :type "range")] (html/set-attr :name (str "poll-response-" (:id poll)))
  [(html/attr= :type "checkbox")] (html/set-attr :name (str "poll-enabled-" (:id poll)))
  )


(html/deftemplate place-template "html/_layout.html"
  [id]
  [:#content] (html/content (place-snippet (first (find-place-by-id DB id))))
  [:#placeList] (html/content (str "var places=[" (jsonify (find-place-by-id DB id)) "]"))
  [:#memories :div :div] (html/content (map memory-snippet (find-memories-by-place-id DB id)))
  [:#artefacts :div :div] (html/content (map photo-snippet (find-photos-by-place-id DB id)))
  [:#value :div :div] (html/content (map poll-snippet (find-polls DB)))
  )


(defroutes routes
  (GET "/" [] (map-template))
  (GET "/map" [] (map-template))
  (GET "/place/:id{[0-9]+}" [id] (place-template id))
  (POST "/memory/add"
		[memory-text place-id]
		(do
		  (insert-memory! DB place-id memory-text)
		  (redirect (str "/place/" place-id))
		  ))
  (POST "/photo/add"
		[photo-url place-id]
		(do
		  (insert-photo! DB place-id photo-url)
		  (redirect (str "/place/" place-id))
		  ))
  (resources "/")
  (not-found "Page not found"))


(def app (site routes))

