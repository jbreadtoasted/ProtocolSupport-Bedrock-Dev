package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.clientbound.play.MiddleInventoryClose;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.typeremapper.pe.PEInventory.InvBlock;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.utils.recyclable.RecyclableArrayList;
import protocolsupport.utils.recyclable.RecyclableCollection;

public class InventoryClose extends MiddleInventoryClose {

	@Override
	public RecyclableCollection<ClientBoundPacketData> toData() {
		cache.getInfTransactions().clear();
		RecyclableArrayList<ClientBoundPacketData> packets = RecyclableArrayList.create();
		ClientBoundPacketData serializer = ClientBoundPacketData.create(PEPacketIDs.CONTAINER_CLOSE, connection.getVersion());
		serializer.writeByte(windowId);
		packets.add(serializer);
		if(connection.hasMetadata("peInvBlocks")) {
			packets.addAll(destroyFakeInventory(connection.getVersion(), (InvBlock[]) connection.getMetadata("peInvBlocks")));
			connection.removeMetadata("peInvBlocks");
		}
		return packets;
	}
	
	public static RecyclableArrayList<ClientBoundPacketData> destroyFakeInventory(ProtocolVersion version, InvBlock[] blocks) {
		RecyclableArrayList<ClientBoundPacketData> packets = RecyclableArrayList.create();
		packets.add(BlockChangeSingle.create(version, blocks[0].getPosition(), blocks[0].getTypeData()));
		packets.add(BlockChangeSingle.create(version, blocks[1].getPosition(), blocks[1].getTypeData()));
		return packets;
	}

}
