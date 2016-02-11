(ns keechma-counter.counter-controller
  (:require [keechma.controller :as controller]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defrecord ^{:doc
"Implements the counter controller.

- `params` function returns `true` because this controller should be always running.
- `start` function sets the default counter value (0). `:kv` is used to store any key - value pairs
- `handler` function is waiting for commands on the `in-chan`. When the command comes (if it's `:inc` or `:dec`) it will increment or decrement the counter."
}
  Controller []
  controller/IController
  (params [_ _] true)
  (start [_ params app-db]
    (assoc-in app-db [:kv :counter] 0))
  (handler [_ app-db-atom in-chan _] 
    (controller/dispatcher app-db-atom in-chan
                           {:inc #(swap! app-db-atom update-in [:kv :counter] inc)
                            :dec #(swap! app-db-atom update-in [:kv :counter] dec)})))
