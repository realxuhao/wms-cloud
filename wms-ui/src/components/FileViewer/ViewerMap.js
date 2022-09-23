import ImageViewer from './ImageViewer'
import PdfJsViewer from './PdfJsViewer'
import JsonViewer from './JsonViewer'
import WordViewer from './WordViewer'
import FolderViewer from './FolderViewer'

const layoutMap = {
  'imageViewer': ImageViewer,
  'pdfViewer': PdfJsViewer,
  'jsonViewer': JsonViewer,
  'wordViewer': WordViewer,
  'folderViewer': FolderViewer
}

export default layoutMap
