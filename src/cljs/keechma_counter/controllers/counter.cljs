(ns keechma-counter.controllers.counter
  (:require [keechma.controller :as controller]))

(defrecord
    ^{:doc
"Multimethod selector for the counter controller.

- `params` uses the `:default` method which returns non-nil indicating the counter controller should be always running.
- `start` method sets the default counter value (0). `:kv` is used to store any key - value pairs.
- `handler` method is waiting for commands on the `in-chan`. When the command comes (if it's `:inc` or `:dec`) it will increment or decrement the counter."
      }
    Controller
    [])

(defmethod controller/start Controller
  [controller params app-db]
  (assoc-in app-db [:kv :counter] 0))

(defmethod controller/handler Controller
  [controller app-db-atom in-chan out-chan]
  (controller/dispatcher app-db-atom in-chan
                         {:inc #(swap! app-db-atom update-in [:kv :counter] inc)
                          :dec #(swap! app-db-atom update-in [:kv :counter] dec)}))
