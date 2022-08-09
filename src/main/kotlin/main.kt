/* <<<<<<<<<<<<<<<<<<<<<<<<<<< ПЕРЕЧИСЛЕНИЯ: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

enum class PostTypes {
    POST, COPY, REPLY, POSTPONE, SUGGEST
}

enum class DonutEditModes {
    ALL /*всю информацию о Donut*/,
    DURATION /*время, в течение которого запись будет доступна только платным подписчикам Donut*/
}

enum class AttachmentTypes {
    otherFile,
    PhotoAttachment, AudioAttachment, VideoAttachment, DocAttachment
}
/* <<<<<<<<<<<<<<<<<<<<<<<<<<< Интерфейсы: >>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

interface Attachment {
    val type: AttachmentTypes
}

/* <<<<<<<<<<<<<<<<<<<<<<<<<<< обычные классы (наследники) : >>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

class AudioAttachment (
    override val type: AttachmentTypes = AttachmentTypes.AudioAttachment,
    val audio: Audio) : Attachment

class VideoAttachment (
    override val type: AttachmentTypes = AttachmentTypes.VideoAttachment,
    val video: Video) : Attachment

class PhotoAttachment (
    override val type: AttachmentTypes = AttachmentTypes.PhotoAttachment,
    val photo: Photo) : Attachment

class DocAttachment (
    override val type: AttachmentTypes = AttachmentTypes.DocAttachment,
    val doc: Doc) : Attachment

/* <<<<<<<<<<<<<<<<<<<<<<<<<<< Data-классы: >>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

data class Audio( /* упрощенно */
    val id:Int = 0,
    val url:String,
    val descr:String = ""
)
data class Video( /* упрощенно */
    val id:Int = 0,
    val url:String,
    val descr:String = ""
)
data class Photo( /* упрощенно */
    val id:Int = 0,
    val url:String,
    val descr:String = ""
)
data class Doc( /* упрощенно */
    val id:Int = 0,
    val url:String,
    val descr:String = ""
)

data class Copyright(
    val id: Int,
    val link: String,
    val name: String,
    val Type: String
)

data class Comment( /* это класс для ЕДИНИЧНОГО комментария (не очень понял смысл оригинальной архитектуры - у меня комментарий это текст с подписью, они объединяются в массив внутри поста) */
    val authorId: Int,
    val text: String
)

data class Donut(
    val isDonut: Boolean, /* запись доступна только платным подписчикам */
    val paidDuration: Int, /* время, в течение которого запись будет доступна только платным подписчикам */
    val placeholder: String, /* заглушка для пользователей, которые не оформили подписку. Отображается вместо содержимого записи. */
    val canPublishFreeCopy: Boolean, /* можно ли открыть запись для всех пользователей, а не только подписчиков Donut */
    val editMode: DonutEditModes /* информация о том, какие значения Donut можно изменить в записи */
)

data class Post(
    val id: Int=0, /* Идентификатор записи. */
    val ownerId: Int=0, /* Идентификатор владельца стены */
    val fromId: Int=0, /* Идентификатор автора записи (от чьего имени опубликована запись) */
    val createdBy: Int=0, /* Идентификатор администратора, который опубликовал запись */
    val date: Long=0, /* Время публикации записи в формате unixtime */
    val text: String="", /* Текст записи */
    val replyOwnerId: Int=0, /* Идентификатор владельца записи, в ответ на которую была оставлена текущая */
    val replyPostId: Int=0, /* Идентификатор записи, в ответ на которую была оставлена текущая */
    val friendsOnly: Boolean=false, /* опцией «Только для друзей» */
    val likes: Int = 0, /* количество лайков - УПРОЩЕННО */
    val reposts: Int = 0, /* количество репостов - УПРОЩЕННО */
    val views: Int = 0, /* кол-во просмотров - УПРОЩЕННО */
    val postType: PostTypes = PostTypes.POST, /* Тип записи */
    val signerId: Int=0, /* Идентификатор автора, если запись была опубликована от имени сообщества и подписана пользователем; */
    val canPin: Boolean=false, /* Информация о том, может ли текущий пользователь закрепить запись */
    val canDelete: Boolean=false, /* Информация о том, может ли текущий пользователь удалить запись */
    val canEdit: Boolean=false, /* Информация о том, может ли текущий пользователь редактировать запись */
    val isPinned: Boolean=false, /* Информация о том, что запись закреплена. */
    val markedAsAds: Boolean=false, /* Информация о том, содержит ли запись отметку «реклама» */
    val isFavorite: Boolean=false, /* true, если объект добавлен в закладки у текущего пользователя. */
    val postponedId: Int=0, /* Идентификатор отложенной записи. Это поле возвращается тогда, когда запись стояла на таймере */

    var comments: Array<Comment> = emptyArray<Comment>(), /* массив с комментариями - УПРОЩЕННО */
    val donut: Donut?, /* Информация о записи Donut - УПРОЩЕННО */
    val copyright: Copyright?, /* объект-источник материала - УПРОЩЕННО */

    val attachments: Array<Attachment> = emptyArray<Attachment>()
) {

}

/* <<<<<<<<<<<<<<<<<<<<<<<<<<< СЕРВИСЫ: >>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

object WallService {
    private var posts = emptyArray<Post>()
    private var currentId: Int = 0
    private var wallComments = emptyArray<Comment>()

    fun add(post: Post): Post { /* добавление нового поста с присвоением нового Id */
        currentId += 1
        posts += post.copy(id = currentId)
        return posts.last()
    }

    fun update(post: Post): Boolean { /* Изменение существующего поста, в случае успеха возвращает true */
        var result: Boolean = false;

        for ((index, postInArray) in posts.withIndex()) {
            if (postInArray.id == post.id) {
                posts[index] = post.copy(ownerId = postInArray.ownerId, date = postInArray.date)
                result = true
            }
        }
        return result
    }

    fun likeById(id: Int) {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                posts[index] = post.copy(likes = post.likes + 1)
            }
        }
    }
}

/* <<<<<<<<<<<<<<<<<<<<<<<<<<< ФУНКЦИОНАЛ: >>>>>>>>>>>>>>>>>>>>>>>>>>>>> */

fun main() {
    val firstPost: Post = Post(id=1, text="Какой-то текст", donut = null, copyright = null)
    var arrComments = firstPost.comments
    arrComments += Comment(1,"комментарий")
    val postWithComment = firstPost.copy(comments = arrComments)
    println(postWithComment)

    val postWithAttachment = postWithComment.copy(
                    id = postWithComment.id + 1,
                    attachments = postWithComment.attachments + AudioAttachment(audio = Audio(url="https://...", descr = "музыка")))
    val postWithAttachment2 =postWithAttachment.copy(
                    id = postWithAttachment.id + 1,
                    attachments = postWithAttachment.attachments + DocAttachment(doc = Doc(url="https://...", descr = "документ")))
    println(postWithAttachment2)
}