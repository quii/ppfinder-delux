(ns ppfinder-delux.prod
  (:require [ppfinder-delux.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
