package example.micronaut.logic.operations
import com.example.logic.operations.buyOrders
import com.example.logic.operations.orderMap
import com.example.model.Filled
import com.example.model.OrderResponseSell
import java.math.BigInteger


fun updateFilledOrders(sellOrders: MutableList<OrderResponseSell>, performance:String, salePrice:BigInteger, saleQuantity:BigInteger)
{
    for(orderFilled in orderMap.keys)
    {
        if(orderFilled.orderId == buyOrders[0].orderId)
        {
            orderFilled.filled.add(Filled(salePrice,saleQuantity))
        }
        else if(orderFilled.orderId== sellOrders[0].orderId)
        {
            if(performance=="NORMAL")
                orderFilled.filled.add(Filled(salePrice,saleQuantity))
            else
                orderFilled.filled.add(Filled(salePrice,saleQuantity))
        }
    }
}