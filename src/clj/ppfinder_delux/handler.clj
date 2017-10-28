(ns ppfinder-delux.handler
  (:require [compojure.core :refer [GET defroutes routes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [ppfinder-delux.middleware :refer [wrap-middleware]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h3 "ClojureScript has not been compiled!"]
   [:p "please run "
    [:b "lein figwheel"]
    " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name    "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

(defn data-handler [request]
  (response {:foo "bar", :headers (get-in request [:headers])}))

(defroutes app-routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page))
  (GET "/data" request (data-handler request)))

(defroutes api-routes
  (GET "/" [] {:body {:title "the 'root' route"}})
  (GET "/data" request (data-handler request))
  (GET "/about" [] {:body {:title "About Pretty Print Finder"}}))

(defn content-type
  [content-type route-with-content]
  (fn [request]
    (if (= content-type (get-in request [:headers "content-type"]))
      (route-with-content request))))

(def all-routes
  (routes
   (content-type "application/json" (wrap-json-response api-routes))
   app-routes
   (resources "/")
   (not-found "Not Found")))

(def app (wrap-middleware all-routes))
