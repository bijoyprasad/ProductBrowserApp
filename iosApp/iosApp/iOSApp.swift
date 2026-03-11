import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinInitializerKt.initKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}