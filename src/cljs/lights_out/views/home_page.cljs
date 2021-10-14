(ns lights-out.views.home-page
  (:require [re-frame.core :as rf]

            [lights-out.domain :refer [cell->label]]))

(defn choose-size []
  (let [grid-size @(rf/subscribe [:setup/grid-size])]
    [:div.field.is-horizontal
     [:div.field-label.is-normal>label.label "Grid Size"]
     [:div.field-body>div.field.is-narrow>p.control
      [:input.input {:type "number"
                     :min 1
                     :max 26
                     :on-change #(rf/dispatch [:setup/size-changed (-> % .-target .-value int)])
                     :value grid-size}]]]))

(defn setup-errors [errors]
  (into [:div.setup-errors]
        (map (fn [error] [:div.setup-error error]) errors)))

(defn setup []
  (let [errors @(rf/subscribe [:setup/errors])]
    [:div.box
     [choose-size]
     [:div.control>a.button.is-primary {:on-click #(rf/dispatch [:setup/new-game])} "New Game"]
     (when errors [setup-errors errors])]))

(defn grid-cell
  [size completed? index lit]
  (let [label (cell->label index size)
        click-handler (when-not completed? #(rf/dispatch [:game/toggle-cell index]))
        enter-handler (when-not completed? #(rf/dispatch [:game/enter-cell label]))
        exit-handler  (when-not completed?  #(rf/dispatch [:game/leave-cell]))]
    ^{:key (str index)}
    [:div.cell (-> {:class (if lit "lit" "unlit")

                    :on-click click-handler
                    :on-mouse-enter enter-handler
                    :on-mouse-leave exit-handler})]))

(defn display-game []
  (let [completed @(rf/subscribe [:game/completed?])
        grid-size @(rf/subscribe [:game/grid-size])
        grid @(rf/subscribe [:game/grid])
        hovered-cell @(rf/subscribe [:game/hovered-cell])]
    [:div.column>div.box>div.columns
     [:div.square
      (when completed [:div.success "Success. Now where's the light switch?"])
      [:div.grid-container
       (into [:div.grid
              {:style {:grid-template-columns (str "repeat(" grid-size ", 1fr)")
                       :grid-template-rows    (str "repeat(" grid-size ", 1fr)")}}]
             (map-indexed (partial grid-cell grid-size completed) grid))]]
     [:div.column.is-narrow>div#hovered-cell hovered-cell]]))

(defn history-move
  [size index move]
  
  (let [label @(rf/subscribe [:game/move-label index size])]
    ^{:key (str index)} [:li label]))

(defn display-history []
  (let [history @(rf/subscribe [:game/history])
        grid-size @(rf/subscribe [:game/grid-size])]
    [:div.column.is-narrow>div.box
     [:h2 "Moves"]
     (into [:ol
            (doall (map-indexed (partial history-move grid-size) history))])]))

(defn when-game []
  (let [game @(rf/subscribe [:game/game])]
    (if game 
      [:div.columns
       [display-history]
       [display-game]] 
      [:div.else])))


(defn home-page []
  [:section.section>div.container>div.content
   [setup]
   [when-game]])

