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
  [size index lit]
  (let [label (cell->label index size)]
    ^{:key (str index)}
    [:div.cell {:class (if lit "lit" "unlit")
                :on-click #(rf/dispatch [:game/toggle-cell index])}
     label]))

(defn display-game []
  (let [succeeded? @(rf/subscribe [:game/succeeded?])
        grid-size @(rf/subscribe [:game/grid-size])
        grid @(rf/subscribe [:game/grid])]
    [:div.column>div.box>div.square
     (when succeeded? [:div.success "Success. Now where's the light switch?"])
     [:div.grid-container
      (into [:div.grid
             {:style {:grid-template-columns (str "repeat(" grid-size ", 1fr)")
                      :grid-template-rows    (str "repeat(" grid-size ", 1fr)")}}]
            (map-indexed (partial grid-cell grid-size) grid))]]))

(defn history-move
  [size index move]
  ^{:key (str index)}
  [:li (cell->label move size)])

(defn display-history []
  (let [history @(rf/subscribe [:game/history])
        grid-size @(rf/subscribe [:game/grid-size])]
    [:div.column.is-narrow>:div.box
     [:h2 "Moves"]
     (into [:ol
            (map-indexed (partial history-move grid-size) history)])]))

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

