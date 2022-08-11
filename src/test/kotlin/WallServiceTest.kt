import org.junit.Test
import org.junit.Assert.*
import java.util.*

class WallServiceTest {

    @Test
    fun add() {
        val calendar = Calendar.getInstance()
        val timestamp = calendar.timeInMillis

        var newPost: Post = Post(
            ownerId = 112,
            date = timestamp,
            text = "Какой-то пост",
            postType = PostTypes.POST,
            donut = Donut(false, 0, "", false, DonutEditModes.ALL),
            copyright = null
        )

        newPost = WallService.add(newPost)

        assertEquals(WallService.getCurrentId(), newPost.id)
    }

    @Test
    fun update_RealId() {

        /* предыдущим тестом уже создан экземпляр Post с id=1 */

        val calendar = Calendar.getInstance()
        val timestamp = calendar.timeInMillis

        var newPost: Post = Post(
            id = 1,
            ownerId = 112,
            date = timestamp,
            text = "Новый текст, новый текст!",
            postType = PostTypes.POST,
            donut = Donut(false, 0, "", false, DonutEditModes.ALL),
            copyright = null
        )

        assertEquals(true, WallService.update(newPost))
    }

    @Test
    fun update_NotRealId() {

        /* предыдущим тестом уже создан экземпляр Post с id=1 */

        val calendar = Calendar.getInstance()
        val timestamp = calendar.timeInMillis

        var newPost: Post = Post(
            id = 999,
            ownerId = 112,
            date = timestamp,
            text = "Новый текст, новый текст!!",
            postType = PostTypes.POST,
            donut = Donut(false, 0, "", false, DonutEditModes.ALL),
            copyright = null
        )

        assertEquals(false, WallService.update(newPost))
    }


    @Test(expected = PostNotFoundException::class)
    fun shouldThrow() {
        // здесь код с вызовом функции, которая должна выкинуть PostNotFoundException:

        val calendar = Calendar.getInstance()
        val timestamp = calendar.timeInMillis

        var newPost: Post = Post(
            ownerId = 112,
            date = timestamp,
            text = "Новый текст, новый текст!!",
            postType = PostTypes.POST,
            donut = Donut(false, 0, "", false, DonutEditModes.ALL),
            copyright = null
        )
        WallService.add(newPost)
        //val newComm = WallService.createComment(1, Comment(12,"коммент к последнему посту"))
        val newComm = WallService.createComment(WallService.getCurrentId()+1, Comment(123,"коммент к несуществующему посту"))
    }

    @Test
    fun shouldGetNormalComment() {
        // здесь код с вызовом функции, которая должна выкинуть PostNotFoundException:

        val calendar = Calendar.getInstance()
        val timestamp = calendar.timeInMillis

        var newPost: Post = Post(
            ownerId = 112,
            date = timestamp,
            text = "Новый текст, новый текст!!",
            postType = PostTypes.POST,
            donut = Donut(false, 0, "", false, DonutEditModes.ALL),
            copyright = null
        )
        WallService.add(newPost)
        val newComm = WallService.createComment(WallService.getCurrentId(), Comment(12,"коммент к последнему посту"))
        //val newComm = WallService.createComment(WallService.getCurrentId()+1, Comment(123,"коммент к несуществующему посту"))

        assertEquals("коммент к последнему посту", newComm.text)
    }

}