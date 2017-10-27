(ns ^:figwheel-no-load ppfinder-delux.dev
  (:require
    [ppfinder-delux.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
