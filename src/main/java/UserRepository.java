import com.zugazagoitia.knag.model.User;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

}
