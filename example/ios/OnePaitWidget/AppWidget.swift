//
//  OnePaitWidget.swift
//  OnePaitWidget
//
//  Created by mz01-phunv on 16/7/24.
//

import WidgetKit
import SwiftUI
import iOSOnePaitWidget

struct AppWidget: Widget {
    let kind: String = "OnePaitWidget"

    var body: some WidgetConfiguration {
        return OnePaitWidget.getWidgetConfiguration()
    }
}
