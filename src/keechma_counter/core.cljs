(ns keechma-counter.core
  (:require [keechma-counter.counter-controller :as counter]
            [keechma-counter.main-component :as main-component]
            [keechma.app-state :as app-state])
  (:require-macros [reagent.ratom :refer [reaction]]))

(enable-console-print!)

(defn counter-value-sub
  "Subscription that returns the current value of the counter."
  [app-state]
  (reaction
   (get-in @app-state [:kv :counter])))

(def app-definition
  "Definition of the application.

  - `:controllers` param holds all of the controllers needed to run the app
  - Counter controller is registered under the `:counter` key. Main component
  has the `:counter` `:topic` assoc-ed to it which allows it to send the
  commands to the Counter controller.
  - `:component` param holds all the component needed to render the app
  - `:subscriptions` param holds the application subscriptions"
  {:controllers {:counter (counter/->Controller)}
   :components {:main (assoc main-component/component :topic :counter)}
   :subscriptions {:counter-value counter-value-sub}
   :html-element (.getElementById js/document "app")})
 

(defonce running-app
  "Atom that holds the currently running app map."
  (clojure.core/atom))

(defn start-app!
  "Helper function that starts the application."
  []
  (reset! running-app (app-state/start! app-definition)))

(defn restart-app!
  "Helper function that restarts the application whenever the
  code is hot reloaded."
  []
  (let [current @running-app]
    (if current
      (app-state/stop! current start-app!)
      (start-app!))))

(restart-app!)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
