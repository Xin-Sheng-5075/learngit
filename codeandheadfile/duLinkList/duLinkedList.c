#include "../head/duLinkedList.h"
#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<C:/Users/Lenovo1/Desktop/duLinkedList.h>

/**
 *  @name        : Status InitList_DuL(DuLinkedList *L)
 *	@description : initialize an empty linked list with only the head node
 *	@param		 : L(the head node)
 *	@return		 : Status
 *  @notice      : None
 */
Status InitList_DuL(DuLinkedList *L) {
	return !((*L=(DuLinkedList)malloc(sizeof(DuLNode))))?ERROR:((*L)->prior=NULL,(*L)->next=NULL,SUCCESS);
}

/**
 *  @name        : void DestroyList_DuL(DuLinkedList *L)
 *	@description : destroy a linked list
 *	@param		 : L(the head node)
 *	@return		 : status
 *  @notice      : None
 */
void DestroyList_DuL(DuLinkedList *L) {
	if(!(*L)) return;
	DuLinkedList p1=*L,p2=p1->next;
	while(p2)
	{
		free(p1);
		p1=p2;
		p2=p2->next;
	}
	free(p1);
	return;
}

/**
 *  @name        : Status InsertBeforeList_DuL(DuLNode *p, LNode *q)
 *	@description : insert node q before node p
 *	@param		 : p, q
 *	@return		 : status
 *  @notice      : None
 */
Status InsertBeforeList_DuL(DuLNode *p, DuLNode *q) {
	q->prior = p->prior;
	p->prior->next = q;
	q->next = p;
	p->prior = q;
	return SUCCESS;
}

/**
 *  @name        : Status InsertAfterList_DuL(DuLNode *p, DuLNode *q)
 *	@description : insert node q after node p
 *	@param		 : p, q
 *	@return		 : status
 *  @notice      : None
 */
Status InsertAfterList_DuL(DuLNode *p, DuLNode *q) {
	if(p->next)
	{
		q->next=p->next;
		p->next->prior=q;
		q->prior=p;
		p->next=q;
		return SUCCESS;
	}
	p->next=q;q->prior=p;return SUCCESS;
}

/**
 *  @name        : Status DeleteList_DuL(DuLNode *p, ElemType *e)
 *	@description : delete the first node after the node p and assign its value to e
 *	@param		 : p, e
 *	@return		 : status
 *  @notice      : None
 */
Status DeleteList_DuL(DuLNode *p, ElemType *e) {
	if(!(p->next)) return ERROR;
	if(!(p->next->next))
	{
		*e=p->next->data;
		DuLinkedList tem = p->next;
		p->next=NULL;
		free(tem);
		return SUCCESS;
	}
	DuLinkedList tem = p->next;
	*e=tem->data;
	p->next=tem->next;
	tem->next->prior=p;
	free(tem);
	return SUCCESS;
}

/**
 *  @name        : void TraverseList_DuL(DuLinkedList L, void (*visit)(ElemType e))
 *	@description : traverse the linked list and call the funtion visit
 *	@param		 : L(the head node), visit
 *	@return		 : Status
 *  @notice      : None
 */
void Visit(ElemType e)
{
	printf("%d ",e);
}
void TraverseList_DuL(DuLinkedList L, void (*visit)(ElemType e)) {
	if(!(L) || !(L->next)) return;
	DuLinkedList p=L->next;
	while(p)
	{
		(*visit)(p->data);
		p=p->next;
	}
}


void begininterface()   //???????????????????????????
{
	printf("\n\n");
	printf("\t\t   @   @   @\n\t\t@@@@  @@@  @@@@\n\t\t @@@  @@@  @@@\n\t\t  @@@ @@@ @@@\n\t\t   @@@@@@@@@\n");
	sleep(2);
	printf("\t\t ?????? ");sleep(1);printf("????????????");
}

void adminmenu()
{
	printf("**************************************************************\n\
*                                                            *\n\
*                       ?????????????????????                       *\n\
*                                                            *\n\
*                           ?????????:                          *\n\
*                                                            *\n\
*                        1.???????????????                        *\n\
*                        2. ????????????                         *\n\
*                                                            *\n\
**************************************************************\n");
}
void adminmenu2()
{
	printf("**************************************************************\n\
*                                                            *\n\
*                                                            *\n\
*                       ?????????????????????                       *\n\
*                                                            *\n\
*                           ?????????:                          *\n\
*                                                            *\n\
*   1.????????????           2.???????????????         3.???????????????   *\n\
*                                                            *\n\
*               4.????????????          5.????????????               *\n\
*                                                            *\n\
*                     6.???????????????????????????                   *\n\
*                                                            *\n\
*                                                            *\n\
*                                                            *\n\
**************************************************************\n");
}
