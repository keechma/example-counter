(ns keechma-counter.ui.main
  (:require [keechma.ui-component :as ui]))

(defn inc-if-odd
  "Increments the counter if the current value is odd."
  [ctx counter-val]
  (when (odd? counter-val)
    (ui/send-command ctx :inc)))

(defn inc-async
  "Increments the counter after 1 second."
  [ctx]
  (.setTimeout js/window #(ui/send-command ctx :inc) 1000))

(defn render
  "Main renderer function. `ctx` argument is partially applied when
  the application is started.

  First we resolve the `counter-sub` subscription which will hold
  the current counter value.

  User can do one of four actions:

  1. Increment the counter
  2. Decrement the counter
  3. Increment the counter if the current value is odd
  4. Increment the counter in async way (after 1 second)

  ---

  Each of these actions sends the command to the controller
  by calling the `(ui/send-command ctx :command) function."
  [ctx]
  (let [counter-sub (ui/subscription ctx :counter-value)]
    (fn []
      [:p
       (str "Clicked: " @counter-sub " times ")
       [:button {:on-click #(ui/send-command ctx :inc)} "+"]
       " "
       [:button {:on-click #(ui/send-command ctx :dec)} "-"]
       " "
       [:button {:on-click #(inc-if-odd ctx @counter-sub)} "Increment if odd"]
       " "
       [:button {:on-click #(inc-async ctx)} "Increment async"]])))

(def component
  "Definition of the component. This component depends on one subscription:
  `counter-value`. This subscription will be resolved on the record when the
  application is started.

  After that the record will be partially applied to the `:renderer` function
  which will allow it to resolve it from the function body and read the value."
  (ui/constructor {:renderer render
                   :subscription-deps [:counter-value]}))
