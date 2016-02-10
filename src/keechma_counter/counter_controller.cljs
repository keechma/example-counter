(ns keechma-counter.counter-controller
  (:require [keechma.controller :as controller]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defrecord Controller []
  controller/IController
  (params [_ _] true)
  (start
    [_ params app-db]
    (assoc-in app-db [:kv :counter] 0))
  (handler [_ app-db-atom in-chan _] 
    (go (loop []
          (let [[command args] (<! in-chan)]
            (case command
              :inc (swap! app-db-atom update-in [:kv :counter] inc)
              :dec (swap! app-db-atom update-in [:kv :counter] dec)
              nil)
            (when command (recur)))))))
