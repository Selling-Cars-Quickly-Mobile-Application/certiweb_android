package pe.edu.pe.certiweb_android.public.pages.pdf

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CarPdfViewer(urlOrData: String) {
    val data = remember(urlOrData) { if (urlOrData.isNotBlank()) urlOrData else PdfState.data ?: "" }
    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            val content = if (data.startsWith("http")) {
                "https://docs.google.com/gview?embedded=1&url=$data"
            } else {
                // For base64 data URIs
                data
            }
            loadUrl(content)
        }
    })
}
