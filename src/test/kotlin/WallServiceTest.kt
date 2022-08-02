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

        assertEquals(1, newPost.id)
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
}