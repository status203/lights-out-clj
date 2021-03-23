(ns lights-out.pages.home-page
  (:require [re-frame.core :as rf]))

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

(defn setup []
  [:div.box
   [choose-size]])

(defn grid-cell [row col]
  (let [key (str row "-" col)]
    ^{:key key} [:div.cell key]))

(defn grid []
  (let [grid-size @(rf/subscribe [:setup/grid-size])
        board @(rf/subscribe [:game/board])]
    [:div.box>div.square>div.grid-container
     (into [:div.grid
            {:style {:grid-template-columns (str "repeat(" grid-size ", 1fr)")
                     :grid-template-rows    (str "repeat(" grid-size ", 1fr)")}}]
           (map-indexed (fn [i lit] ^{:key (str i)} [:div.cell {:class (if lit "lit" "unlit")}]) board)
           )]))

(defn home-page []
  [:section.section>div.container>div.content
   [setup]
   [grid]])

