/*
 * Copyright (C) 2026 piko <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */

package app.crimera.patches.instagram.entity.decoder

import app.crimera.patches.instagram.utils.Constants.EDIT_MEDIA_INFO_FRAGMENT_CLASS
import app.morphe.patcher.Fingerprint
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

object EditMediaInfoGetCurrentMediaIdFingerprint : Fingerprint(
    definingClass = EDIT_MEDIA_INFO_FRAGMENT_CLASS,
    returnType = "Ljava/lang/String;",
    custom = { method, _ -> method.currentMediaIndexFieldOrNull() != null },
)

internal fun Method.currentMediaIndexFieldOrNull(): FieldReference? {
    if (definingClass != EDIT_MEDIA_INFO_FRAGMENT_CLASS || returnType != "Ljava/lang/String;") return null
    val instrs = implementation?.instructions ?: return null

    var stateField: FieldReference? = null
    var indexField: FieldReference? = null
    var hasListSize = false

    for (instruction in instrs) {
        when (instruction.opcode) {
            Opcode.IGET_OBJECT -> {
                val field = instruction.getReference<FieldReference>()
                if (field != null && field.definingClass == definingClass && field.type != "Ljava/util/List;") {
                    stateField = field
                }
            }
            Opcode.IGET -> {
                val field = instruction.getReference<FieldReference>()
                if (field != null && field.type == "I" && field.definingClass != definingClass) {
                    if (stateField == null || field.definingClass == stateField.type) {
                        indexField = field
                    }
                }
            }
            Opcode.INVOKE_INTERFACE, Opcode.INVOKE_VIRTUAL -> {
                val method = instruction.getReference<MethodReference>()
                if (method != null && method.name == "size" && method.returnType == "I") {
                    hasListSize = true
                }
            }
            else -> {}
        }
    }

    if (indexField != null && hasListSize) {
        return indexField
    }
    return null
}

object CommentButtonOnClickFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("select_comment_screen_delete_comments_tap", "comment_share_click"),
)

internal object UserTagInfoDictInitFingerprint : Fingerprint(
    definingClass = "Lcom/instagram/api/schemas/UserTagInfoDict;",
    name = "<init>",
)

object ReelsInlineQualitySurveyRelatedFingerprint : Fingerprint(
    strings = listOf("reels_inline_quality_survey"),
    parameters = listOf(MEDIA_CLASS_NAME),
)
