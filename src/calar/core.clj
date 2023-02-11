(ns calar.core
  (:require [calar.scratcher :as scratcher])
  (:gen-class))


(defn -main []
  (println
   (scratcher/get-subgroups
    (scratcher/get-edn-schedule ((comp :do :forms) scratcher/config) "knt" "351"))))
