package com.droidcon.voices.provider

import androidx.core.content.FileProvider
import com.droidcon.voices.R

/**
 * FileProvider to enable sharing of voice files with other apps
 */
class VoiceFileProvider : FileProvider(R.xml.file_paths)